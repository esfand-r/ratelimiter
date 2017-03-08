package ratelimiter

/**
 * Created by esfandiaramirrahimi on 15-11-03.
 */
trait RateLimiter {
  def increment(clientIP: String): Boolean

  def increment(clientIP: String, count: Int): Boolean
}
