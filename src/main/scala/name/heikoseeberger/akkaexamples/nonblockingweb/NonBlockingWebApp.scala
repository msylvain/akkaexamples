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

import akka.actor.{ ActorSystem, Props }
import spray.can.server.HttpServer
import spray.io.{ IOBridge, SingletonHandler }

object NonBlockingWebApp extends App {

  val system = ActorSystem("akkaexamples-non-blocking-web")
  val ioBridge = new IOBridge(system).start()
  val wordCounter = system.actorOf(Props[WordCounter])
  val translator = system.actorOf(Props[Translator])
  val handler = system.actorOf(Props(new TextService(wordCounter, translator)))
  val server = system.actorOf(Props(new HttpServer(ioBridge, SingletonHandler(handler))), "http-server")
  server ! HttpServer.Bind("localhost", 8080)

  system.registerOnTermination(ioBridge.stop())
  println("Press ENTER to exit")
  Console.readLine()
  system.shutdown()
}
