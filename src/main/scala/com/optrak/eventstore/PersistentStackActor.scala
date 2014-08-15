package com.optrak.eventstore

import akka.actor._
import akka.persistence._
import org.joda.time._

sealed trait StackCommand
case class PushItem(item: String) extends StackCommand
case object PopItem extends StackCommand

sealed abstract class StackEvent (
    val item: String,
    val timestamp: DateTime,
    val remainingItems: List[String]
)
case class ItemPushed(
    override val item: String,
    override val timestamp: DateTime,
    override val remainingItems: List[String]
) extends StackEvent(item, timestamp, remainingItems)
case class ItemPopped(
    override val item: String,
    override val timestamp: DateTime,
    override val remainingItems: List[String]
) extends StackEvent(item, timestamp, remainingItems)

class PersistentStackActor extends PersistentActor {
  override def persistenceId = "sample-id-1"
  type State = List[String]
 
  var state: State = Nil
 
  def updateState(event: StackEvent): Unit = event match {
    case ItemPushed(item, timestamp, remainingItems) => state = item :: state
    case ItemPopped(item, timestamp, remainingItems) => state = state.tail
  }
 
  val receiveRecover: Receive = {
    case evt: StackEvent                   => updateState(evt)
    case SnapshotOffer(_, snapshot: State) => state = snapshot
  }
 
  val receiveCommand: Receive = {
    case PushItem(item) =>
      persist(ItemPushed(item, DateTime.now, item :: state))(updateState)
    case PopItem =>
      val popped = state.head
      persist(ItemPopped(popped, DateTime.now, state.tail))(evt => {
	updateState(evt)
	System.out.println(s"Popped $popped")
	sender ! popped
      })
    case "snap"  => saveSnapshot(state)
    case "print" => System.out.println(state)
  }
 
} 
