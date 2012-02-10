/*
 * Copyright 2011-2012 Heiko Seeberger
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

package name.heikoseeberger.akkaexamples.simple

import akka.actor.{ Actor, ActorSystem, Props }

object Simple {

  def main(args: Array[String]): Unit = {
    ActorSystem("simple").actorOf(Props[PrintActor], "print") ! "Hello"
  }
}

class PrintActor extends Actor {

  override def receive = {
    case "Hello" => context.actorOf(Props[ReverseActor], "reverse") ! "Hello world!"
    case message =>
      println(message)
      context.system.shutdown()
  }
}

class ReverseActor extends Actor {

  override def receive = {
    case message =>
      sender ! message.toString.reverse
      context.stop(self)
  }
}
