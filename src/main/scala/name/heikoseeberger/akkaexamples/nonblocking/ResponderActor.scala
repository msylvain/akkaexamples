/*
 * Copyright 2011 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.heikoseeberger.akkaexamples.nonblocking

import org.jboss.netty.handler.codec.http.HttpResponse
import akka.actor.{ Actor, Props }
import unfiltered.Async
import unfiltered.response.{ PlainTextContent, ResponseString }
import akka.actor.Props

object ResponderActor {

  case class RespondTo(text: String)

  case class WordCount(number: Int)

  case class TranslatedText(text: String)
}

class ResponderActor(responder: Async.Responder[HttpResponse]) extends Actor {
  import ResponderActor._

  private var number: Option[Int] = None

  private var text: Option[String] = None

  override protected def receive = {
    case RespondTo(text) =>
      create(new WordCountActor) ! WordCountActor.CountWords(text, self)
      create(new TranslationActor) ! TranslationActor.TranslateText(text, self)
    case WordCount(number) =>
      this.number = Some(number)
      replyWhenDone()
    case TranslatedText(text) =>
      this.text = Some(text)
      replyWhenDone()
  }

  private def replyWhenDone() =
    for {
      n <- number
      t <- text
    } {
      responder.respond(PlainTextContent ~> ResponseString("%s words have been translated to: %s".format(n, t)))
      self.stop()
    }
}
