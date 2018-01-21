package com.raquo.laminar.experimental.airstream.core

import com.raquo.laminar.experimental.airstream.ownership.Owner

/** LazyObservable only starts when it gets its first observer (internal or external),
  * and stops when it loses its last observer (again, internal or external).
  *
  * Stream and Signal are lazy observables. State is not.
  */
trait LazyObservable[+A, S[+_] <: LazyObservable[_, S]] extends Observable[A] {

  /** Basic idea: Lazy Observable only holds references to those children that have any observers
    * (either directly on themselves, or on any of their descendants). What this achieves:
    * - Stream only propagates its value to children that (directly or not) have observers
    * - Stream calculates its value only once regardless of how many observers / children it has)
    * (so, all streams are "hot" observables)
    * - Stream doesn't hold references to Streams that no one observes, allowing those Streams
    * to be garbage collected if they are otherwise unreachable (which they should become
    * when their subscriptions are killed by their owners)
    */

  def map[B](project: A => B): S[B]

  def compose[B](operator: S[A] => S[B]): S[B]

  def combineWith[AA >: A, B](otherObservable: S[B]): S[(AA, B)]

  override def addObserver(observer: Observer[A])(implicit subscriptionOwner: Owner): Subscription = {
    val subscription = super.addObserver(observer)
    maybeStart()
    subscription
  }

  /** Note: To completely disconnect an Observer from this Observable,
    * you need to remove it as many times as you added it to this Observable.
    *
    * @return whether observer was removed (`false` if it wasn't subscribed to this observable)
    */
  override def removeObserver(observer: Observer[A]): Boolean = {
    val removed = super.removeObserver(observer)
    if (removed) {
      maybeStop()
    }
    removed
  }

  /** Child stream calls this to declare that it was started */
  override protected[airstream] def addInternalObserver(observer: InternalObserver[A]): Unit = {
    super.addInternalObserver(observer)
    maybeStart()
  }

  /** Child stream calls this to declare that it was stopped */
  override protected[airstream] def removeInternalObserver(observer: InternalObserver[A]): Boolean = {
    val removed = super.removeInternalObserver(observer)
    if (removed) {
      maybeStop()
    }
    removed
  }

  private[this] def maybeStart(): Unit = {
    val isStarting = numAllObservers == 1
    if (isStarting) {
      onStart()
    }
  }

  private[this] def maybeStop(): Unit = {
    val isStopping = numAllObservers == 0
    if (isStopping) {
      onStop()
    }
  }

  private[this] def numAllObservers: Int = {
    externalObservers.length + internalObservers.length
  }
}
