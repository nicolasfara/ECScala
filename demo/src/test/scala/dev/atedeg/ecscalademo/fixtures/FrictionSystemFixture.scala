package dev.atedeg.ecscalademo.fixtures

import dev.atedeg.ecscala.util.types.given
import dev.atedeg.ecscala.dsl.ECScalaDSL
import dev.atedeg.ecscalademo.{ EnvironmentState, PlayState }
import dev.atedeg.ecscalademo.systems.FrictionSystem
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock

trait FrictionSystemFixture extends ECScalaDSL with WorldFixture with WorldStateFixture {
  val frictionSystem = new FrictionSystem(playState, environmentState)
  world hasA system(frictionSystem)
  when(environmentState.frictionCoefficient) thenReturn 1.0
  when(environmentState.wallRestitution) thenReturn 0.5
  when(environmentState.gravity) thenReturn 9.81
}
