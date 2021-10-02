package dev.atedeg.ecscalademo.systems

import dev.atedeg.ecscala.World
import dev.atedeg.ecscala.dsl.ECScalaDSL
import dev.atedeg.ecscala.util.types.given
import dev.atedeg.ecscalademo.*
import dev.atedeg.ecscalademo.fixtures.BallSelectionSystemFixture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

import scala.language.implicitConversions

class BallSelectionSystemTest extends AnyWordSpec with Matchers with ECScalaDSL {

  "A BallSelectionSystem" when {
    "the game is paused and the mouse is clicked" should {
      "be enabled" in new BallSelectionSystemFixture {
        enableSystemCondition(playState, mouseState)
        ballSelectionSystem.shouldRun shouldBe true
      }
    }
    "the game is paused and the mouse is not clicked" should {
      "not be enabled" in new BallSelectionSystemFixture {
        disableSystemCondition(playState, mouseState)
        ballSelectionSystem.shouldRun shouldBe false
      }
    }
    "the game is running" should {
      "not be enabled" in new BallSelectionSystemFixture {
        playState.gameState = State.Pause
        ballSelectionSystem.shouldRun shouldBe false
      }
    }
    "a ball is selected" should {
      "set the ball as currently selected" in new BallSelectionSystemFixture {
        enableSystemCondition(playState, mouseState)
        playState.selectedBall = None

        val entity1 = world hasAn entity withComponents {
          Position(10.0, 10.0) &: Circle(startingState.startingRadius, startingState.startingColor)
        }
        val entity2 = world hasAn entity withComponents {
          Position(70.0, 70.0) &: Circle(startingState.startingRadius, startingState.startingColor)
        }

        mouseState.coordinates = Point(10.0, 10.0)
        world.update(10)
        playState.selectedBall shouldBe Some(entity1)

        mouseState.coordinates = Point(65.0, 65.0)
        world.update(10)
        playState.selectedBall shouldBe Some(entity2)
      }
    }
  }

  private def enableSystemCondition(playState: PlayState, mouseState: MouseState): Unit = {
    playState.gameState = State.SelectBall
    mouseState.down = true
  }

  private def disableSystemCondition(playState: PlayState, mouseState: MouseState): Unit = {
    playState.gameState = State.Pause
    mouseState.down = false
  }
}
