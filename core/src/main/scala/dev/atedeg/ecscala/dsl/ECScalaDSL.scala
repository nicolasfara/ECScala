package dev.atedeg.ecscala.dsl

import dev.atedeg.ecscala.util.types.{ CListTag, ComponentTag }
import dev.atedeg.ecscala.{ CList, CNil, Component, Deletable, DeltaTime, Entity, System, View, World }
import dev.atedeg.ecscala.dsl.Words.*

/**
 * This trait provides a domain specific language (DSL) for expressing the ECScala framework operations using an
 * english-like syntax. Here's the things you can do:
 *
 * '''Create an Entity in a World:'''
 * {{{
 * val world = World()
 * val entity1 = world hasAn entity
 * }}}
 *
 * '''Remove Entities from a World:'''
 * {{{
 *   *  world - entity1
 *   *  remove (entity1) from world
 *   *  remove (List(entity1, entity2, entity3)) from world
 * }}}
 *
 * '''Create an Entity in a World with a Component:'''
 * {{{
 * val entity1 = world hasAn entity withComponent MyComponent()
 * }}}
 *
 * '''Create an Entity in a World with multiple Components:'''
 * {{{
 * val entity1 = world hasAn entity withComponents {
 *       MyComponent1() &: MyComponent2() &: MyComponent3()
 * }
 * }}}
 *
 * '''Add Components to an Entity:'''
 * {{{
 *   *  entity1 += MyComponent()
 *   *  entity1 withComponent MyComponent()
 *   *  entity1 withComponents { MyComponent1() &: MyComponent2() }
 * }}}
 *
 * '''Remove Components from an Entity:'''
 * {{{
 *   *  remove { MyComponent() } from entity1
 *   *  entity1 -= MyComponent()
 *   *  remove { MyComponent1() &: MyComponent2() &: MyComponent3() } from entity1
 * }}}
 *
 * '''Add a System to a World:'''
 * {{{
 *     * world hasA system[MyComponent &: CNil] { (_,_,_) => {}}
 *     * world hasA system(MySistem())
 * }}}
 *
 * '''Remove a System from a World'''
 * {{{
 *   remove MySystem() from world
 * }}}
 *
 * '''Get a View from a World:'''
 * {{{
 *   val view = getView[MyComponent1 &: MyComponent2 &: CNil] from world
 * }}}
 *
 * '''Get a View without certain Components'''
 * {{{
 *   val view = getView[MyComponent1 &: CNil].excluding[MyComponent2 &: CNil] from world
 * }}}
 *
 * '''Remove all Entities and their Components from a World:'''
 * {{{
 *   clearAll from world
 * }}}
 */
trait ECScalaDSL extends ExtensionMethods with Conversions with FromSyntax {

  /**
   * Keyword that enables the use of the word "entity" in the dsl.
   */
  def entity: EntityWord = EntityWord()

  /**
   * Keyword that enables the use of the word "system" in the dsl.
   */
  def system[L <: CList](system: System[L])(using clt: CListTag[L])(using world: World): Unit =
    world.addSystem(system)(using clt)

  /**
   * Keyword that enables the use of the word "getView" in the dsl.
   */
  def getView[L <: CList](using clt: CListTag[L]): ViewFromWorld[L] = ViewFromWorld(using clt)

  /**
   * Keyword that enables the use of the word "remove" in the dsl.
   */
  def remove[L <: CList: CListTag](componentsList: L): From[Entity, Unit] = FromEntity(componentsList)

  /**
   * Keyword that enables the use of the word "remove" in the dsl.
   */
  def remove(entities: Seq[Entity]): From[World, Unit] = EntitiesFromWorld(entities)

  /**
   * Keyword that enables the use of the word "remove" in the dsl.
   */
  def remove[L <: CList: CListTag](system: System[L]): From[World, Unit] = SystemFromWorld(system)

  /**
   * Keyword that enables the use of the word "clearAll" in the dsl.
   */
  def clearAll: From[World, Unit] = ClearAllFromWorld(ClearWord())
}

private[dsl] trait FromSyntax {

  /**
   * This trait enables the use of the word "from" in the dsl
   */
  trait From[A, B] {
    def from(elem: A): B
  }

  /**
   * This case class enables the following syntax:
   * {{{
   *   remove (entity1, entity2) from world
   * }}}
   */
  class EntitiesFromWorld(entities: Seq[Entity]) extends From[World, Unit] {
    override def from(world: World): Unit = entities foreach { world.removeEntity(_) }
  }

  /**
   * This case class enables the following syntax:
   * {{{
   *   remove (system1) from world
   * }}}
   */
  class SystemFromWorld[L <: CList: CListTag](system: System[L]) extends From[World, Unit] {
    override def from(world: World): Unit = world.removeSystem(system)
  }

  /**
   * This case class enables the following syntax:
   * {{{
   *   clearAll from world
   * }}}
   */
  class ClearAllFromWorld(clearWord: ClearWord) extends From[World, Unit] {
    override def from(world: World): Unit = world.clear()
  }

  /**
   * This case class enables the following syntax:
   *
   * {{{
   *   remove (myComponent) from entity1
   * }}}
   */
  class FromEntity[L <: CList](componentList: L)(using clt: CListTag[L]) extends From[Entity, Unit] {

    override def from(entity: Entity): Unit =
      componentList zip clt.tags.asInstanceOf[Seq[ComponentTag[Component]]] foreach {
        entity.removeComponent(_)(using _)
      }
  }

  /**
   * This case class enables the following syntax:
   *
   * {{{
   *   * getView[MyComponent1 &: MyComponent2 &: CNil] from world
   *   * getView[MyComponent1 &: MyComponent2 &: CNil].exluding[MyComponent3 &: CNil] from world
   * }}}
   */
  class ViewFromWorld[A <: CList](using cltA: CListTag[A]) extends From[World, View[A]] {
    override def from(world: World): View[A] = world.getView(using cltA)

    def excluding[B <: CList](using cltB: CListTag[B]): ExcludingViewFromWorld[A, B] = ExcludingViewFromWorld(using
      cltA,
    )(using cltB)
  }

  class ExcludingViewFromWorld[A <: CList, B <: CList](using cltA: CListTag[A])(using cltB: CListTag[B])
      extends From[World, View[A]] {
    def from(world: World) = world.getView(using cltA, cltB)
  }
}

object Words {

  /**
   * This case class enables the following syntax:
   *
   * {{{
   * world hasAn entity
   * }}}
   */
  case class EntityWord()

  /**
   * This case class enables the following syntax:
   *
   * {{{
   * clearAll from world
   * }}}
   */
  private[dsl] case class ClearWord()
}
