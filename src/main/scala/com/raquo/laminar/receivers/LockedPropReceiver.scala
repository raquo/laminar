package com.raquo.laminar.receivers

import com.raquo.domtypes.generic.keys.Prop
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.xstream.XStream
import org.scalajs.dom

/** A Property receiver that is locked to a particular element */
class LockedPropReceiver[V, DomV](
  prop: Prop[V, DomV],
  element: ReactiveElement[dom.Element]
) {

  def <--($value: XStream[V]): Unit = {
    (new PropReceiver(prop) <-- $value)(element)
  }
}