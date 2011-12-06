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

import akka.actor.{ ActorSystem, Props }
import unfiltered.netty.async.Planify
import unfiltered.netty.Http
import unfiltered.request.Params

object TextServer {

  def main(args: Array[String]): Unit = {
    println("Waiting for your requests ...")
    Http(8080).plan(plan).run()
  }

  private def plan = Planify {
    case request @ Params(NonEmptyText(text)) =>
      create(new ResponderActor(request)) ! ResponderActor.RespondTo(text)
  }

  private object NonEmptyText extends Params.Extract("text", Params.first ~> Params.nonempty)
}
