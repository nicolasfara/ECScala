package dev.atedeg.ecscalademo.systems

import dev.atedeg.ecscala.*
import dev.atedeg.ecscala.util.types.given
import dev.atedeg.ecscala.CNil
import dev.atedeg.ecscalademo.{ PlayState, Position, Velocity }
import dev.atedeg.ecscalademo.*

/**
 * The [[System]] that updates the balls Positions given the updated Velocities
 */
class MovementSystem(private val playState: PlayState) extends System[Position &: Velocity &: CNil] {
  override def shouldRun: Boolean = playState.gameState == State.Play

  override def update(entity: Entity, components: Position &: Velocity &: CNil)(
      deltaTime: DeltaTime,
      world: World,
      view: View[Position &: Velocity &: CNil],
  ): Deletable[Position &: Velocity &: CNil] = {
    val Position(position) &: Velocity(velocity) &: CNil = components
    val newPosition = position + (deltaTime * velocity)
    Position(newPosition) &: Velocity(velocity) &: CNil
  }
}
