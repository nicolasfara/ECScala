package dev.atedeg.ecscalademo.systems

import scala.language.implicitConversions
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.Inspectors.forAll
import org.scalatestplus.mockito.MockitoSugar.mock
import org.mockito.Mockito.when
import dev.atedeg.ecscala.{&:, CNil, World}
import dev.atedeg.ecscala.util.types.given
import dev.atedeg.ecscalademo.{ECSCanvas, EnvironmentState, PlayState, Point, Vector, Position, Velocity, given}
import dev.atedeg.ecscalademo.fixtures.CollisionsFixture
import dev.atedeg.ecscalademo.util.WritableSpacePartitionContainer
import scalafx.scene.paint.Color

class WallCollisionSystemTest extends AnyWordSpec with Matchers {

  "The WallCollisionSystem" should {
    "keep entities inside the canvas's borders" in new CollisionsFixture {
      PlayState.playing = true
      val canvas = mock[ECSCanvas]
      when(canvas.width) thenReturn 100.0
      when(canvas.height) thenReturn 100.0
      world addSystem (new WallCollisionSystem(canvas))
      world.update(1)
      val view = world.getView[Position &: Velocity &: CNil]
      forAll(view map (_._2)) { comps =>
          val position &: _ &: CNil = comps
          position.x should (be >= 10.0 and be <= 90.0)
          position.y should (be >= 10.0 and be <= 90.0)
      }
    }
    "change velocities to entities that collide with the canvas's borders" in new CollisionsFixture {
      PlayState.playing = true
      EnvironmentState.wallRestitution = 1
      val canvas = mock[ECSCanvas]
      when(canvas.width) thenReturn 100.0
      when(canvas.height) thenReturn 100.0
      world addSystem (new WallCollisionSystem(canvas))
      world.update(1)
      val view = world.getView[Position &: Velocity &: CNil]
      forAll(view map (_._2)) { comps =>
        val Position(Point(x, y)) &: Velocity(velocity) &: CNil = comps
        velocity shouldBe Vector(if x == 90.0 then -1 else 1, if y == 90.0 then -1 else 1)
      }
    }
  }
}
