package com.optrak.eventstore

import akka.actor.ActorSystem 
import org.specs2.mutable._

class LowLevelTest extends Specification with Logging {
  "basic write" should {
    "match input" in {
      val joe = Simple("joseph")

      val simpleFields = "name" :: HNil

      val simpleWriter = writer[Simple](simpleFields)
      val joeAST = simpleWriter.write(joe)

      //logger.debug("simple object in JSON: " + pretty(render(joeAST)))
      joeAST mustEqual JObject(List(("name",JString("joseph"))))

    }
  }
}