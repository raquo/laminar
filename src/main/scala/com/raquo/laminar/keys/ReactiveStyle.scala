package com.raquo.laminar.keys

import com.raquo.airstream.core.Source
import com.raquo.domtypes.generic.keys.Style
import com.raquo.laminar.DomApi
import com.raquo.laminar.api.Laminar.{HtmlElement, optionToSetter}
import com.raquo.laminar.modifiers.KeyUpdater.StyleUpdater
import com.raquo.laminar.modifiers.{KeySetter, KeyUpdater, Setter}

import scala.scalajs.js.|

// @TODO[API] idea for style syntax: display(_.inlineBlock) – where `_` could be coming from either display itself or some other object

/** Note: Unlike other reactive keys, this has to be a wrapping value class because Scala DOM Types
  *       instantiates its own Style objects. It is a known deficiency in its API, see
  *       https://github.com/raquo/scala-dom-types/issues/2
  */
class ReactiveStyle[V](val style: Style[V]) extends AnyVal {

  // @TODO[API] Should this accept V or V | String?
  @inline def apply(value: V): Setter[HtmlElement] = {
    this := value
  }

  def maybe(value: Option[V | String]): Setter[HtmlElement] = {
    value.map(v => this := v)
  }

  def :=(value: V | String): Setter[HtmlElement] = {
    new KeySetter[Style[V], V | String, HtmlElement](style, value, DomApi.setHtmlAnyStyle)
  }

  def :=(value: String): Setter[HtmlElement] = {
    new KeySetter[Style[V], String, HtmlElement](style, value, DomApi.setHtmlStringStyle)
  }

  def <--[A]($value: Source[A])(implicit ev: A => V | String): StyleUpdater[V] = {
    new KeyUpdater[HtmlElement, Style[V], V | String](
      style,
      $value.asInstanceOf[Source[V | String]].toObservable,
      (el, v) => DomApi.setHtmlAnyStyle(el, style, v)
    )
  }

}
