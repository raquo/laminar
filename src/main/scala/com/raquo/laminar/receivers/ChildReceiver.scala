package com.raquo.laminar.receivers

import com.raquo.airstream.core.Source
import com.raquo.laminar.modifiers.ChildrenInserter.Child
import com.raquo.laminar.modifiers.{ChildInserter, Inserter}
import com.raquo.laminar.nodes.ReactiveElement

object ChildReceiver {

  val maybe: MaybeChildReceiver.type = MaybeChildReceiver

  val text: TextChildReceiver.type = TextChildReceiver

  @deprecated("Use child.text instead of child.int, it can handle integers too now.", "0.12.0")
  val int: TextChildReceiver.type = TextChildReceiver

  def <--($node: Source[Child]): Inserter[ReactiveElement.Base] = {
    ChildInserter[ReactiveElement.Base](
      _ => $node.toObservable,
      initialInsertContext = None
    )
  }
}
