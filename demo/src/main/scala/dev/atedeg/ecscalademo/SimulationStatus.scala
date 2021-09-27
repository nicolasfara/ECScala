package dev.atedeg.ecscalademo

import dev.atedeg.ecscala.Entity

object MouseState {
  var coordinates = Point(0, 0)
  var clicked = false
  var down = false
  var up = false
}

object PlayState {
  var playing = false
  var selectedBall: Option[Entity] = Option.empty
  var addBallMode: Boolean = false
  var velocityEditingMode: Boolean = false
}

object StartingState {
  val startingRadius: Double = 20.0
  val startingColor: Color = Color(255, 255, 0)
  val startingMass: Double = 1
  val startingVelocity: Vector = Vector(0.0, 0.0)

  val startingPositions = List(
    Position(Point(147, 157)),
    Position(Point(511, 157)),
    Position(Point(546, 177)),
    Position(Point(546, 136)),
    Position(Point(581, 157)),
    Position(Point(581, 117)),
    Position(Point(581, 198)),
  )

  val startingColors = List(
    Color(255, 255, 255),
    Color(255, 215, 0),
    Color(0, 0, 255),
    Color(255, 0, 0),
    Color(75, 0, 130),
    Color(255, 69, 0),
    Color(34, 139, 34),
  )

  val startingVelocities = List(
    Velocity(Vector(1000, 0)),
    Velocity(Vector(0, 0)),
    Velocity(Vector(0, 0)),
    Velocity(Vector(0, 0)),
    Velocity(Vector(0, 0)),
    Velocity(Vector(0, 0)),
    Velocity(Vector(0, 0)),
  )
}

object EnvironmentState {
  val frictionCoefficient: Double = 0.05
  var wallRestitution: Double = 0.5
  val gravity: Double = 9.81
}
