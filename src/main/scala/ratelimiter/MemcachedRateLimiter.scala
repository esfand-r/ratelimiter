package ratelimiter

import javax.inject.Named

import net.spy.memcached.{AddrUtil, MemcachedClient}

import scala.collection.JavaConverters._

/**
 * Created by esfandiaramirrahimi on 15-11-03.
 */
@Named
class MemcachedRateLimiter extends RateLimiter {
  val l: List[String] = List()
  val memcachedClient: MemcachedClient = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"))
  val rateLimit = 2
  val rateDurationMillisecond: Long = 2 * 1000
  // 2 seconds
  val timeSlicer = new TimeSlicer(2, 2)

  override def increment(clientIP: String): Boolean = {
    increment(clientIP, 1)
  }

  @Override
  override def increment(clientIP: String, count: Int): Boolean = {
    val incrementedValue: Long = incr(clientIP, count)

    val keys: List[String] = getSliceKeys(clientIP)
    val ll = l :: keys
    println("keys for all the calls so far: " + keys.mkString(","))

    println("retrieved keys: " + keys.mkString(","))
    val slices = memcachedClient.getBulk(keys.asJava).asScala
    println("retrieved slices: " + slices.values.map(a => a.asInstanceOf[String].toInt).mkString(","))

    val sumOfBuckets: Long = slices.values.map(a => a.asInstanceOf[String].toInt).sum
    println("Sum of buckets: " + sumOfBuckets)

    var canIncrement: Boolean = true
    if (sumOfBuckets > this.rateLimit) {
      canIncrement = false
    }

    canIncrement
  }

  private def getSliceKeys(clientIP: String): List[String] = {
    val keys: List[String] = timeSlicer.getSliceKeys
    keys.map(key => clientIP + "-" + key)
  }

  private def incr(clientIP: String, count: Int): Long = {
    val expiresIn: Int = (rateDurationMillisecond / 1000 + 60).toInt //1 minute padding for expiry to be on the safe side.
    val key = clientIP + "-" + timeSlicer.getCurrentSliceKey
    val inc = memcachedClient.incr(key, count, count, expiresIn)
    println("incrementing for key " + key + " and gotten new value=" + inc)
    inc
  }
}
