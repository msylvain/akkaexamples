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

package name.heikoseeberger.akkaexamples.simple

import akka.actor.{ Actor, ActorSystem }

object Simple {

  def main(args: Array[String]): Unit = {
    ActorSystem().actorOf[PrintActor] ! "Hello"
  }
}

class PrintActor extends Actor {

  override def receive = {
    case "Hello" => system.actorOf[ReverseActor] ! "Hello world!"
    case message =>
      println(message)
      system.stop()
  }
}

class ReverseActor extends Actor {

  override def receive = {
    case message =>
      sender ! message.toString.reverse
      self.stop()
  }
}
