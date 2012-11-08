/*
 * Copyright 2012 Heiko Seeberger
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

package name.heikoseeberger.akkaexamples
package nonblockingweb

import akka.actor.{ Actor, ActorRef, Props }
import scala.concurrent.duration._
import spray.routing.{ HttpServiceActor, RequestContext }

class TextService(wordCounter: ActorRef, translator: ActorRef) extends Actor with HttpServiceActor {

  def receive = runRoute(
    path("")(
      get(
        parameter("text") { text =>
          requestContext =>
            val textProcessor = // One-off actor
              context.actorOf(Props(new TextProcessor(wordCounter, translator, requestContext)))
            textProcessor ! TextProcessor.ProcessText(text)
        }
      )
    )
  )
}

object TextProcessor {

  case class ProcessText(text: String)

  case class WordCount(number: Int)

  case class Translation(text: String)
}

class TextProcessor(wordCounter: ActorRef, translator: ActorRef, requestContext: RequestContext) extends Actor {

  import TextProcessor._

  var number: Option[Int] = None

  var text: Option[String] = None

  def receive = {
    case ProcessText(text) =>
      context.actorOf(Props[WordCounter]) ! text // One-off actor
      context.actorOf(Props[Translator]) ! text // One-off actor
    case WordCount(n) =>
      number = Some(n)
      replyWhenDone()
    case Translation(t) =>
      text = Some(t)
      replyWhenDone()
  }

  def replyWhenDone() =
    for (n <- number; t <- text) { // We need both number and text
      requestContext.complete(s"$n words have been translated to: $t\n")
      context.stop(self) // Terminate one-off actor
    }
}

class WordCounter extends Actor {

  def receive = {
    case text: String =>
      burnCycles(100 milliseconds)
      sender ! TextProcessor.WordCount(text split " " size)
      context.stop(self) // Terminate one-off actor
  }
}

class Translator extends Actor {

  def receive = {
    case text: String =>
      burnCycles(100 milliseconds)
      sender ! TextProcessor.Translation(text.reverse)
      context.stop(self) // Terminate one-off actor
  }
}
