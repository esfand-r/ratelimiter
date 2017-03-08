import com.google.inject.{AbstractModule, Guice}
import ratelimiter.{MemcachedRateLimiter, RateLimiter}

/**
 * Created by esfandiaramirrahimi on 15-11-03.
 */
object Application {
  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(classOf[RateLimiter]).to(classOf[MemcachedRateLimiter])
    }
  })

  private val rateLimiter = injector.getInstance(classOf[RateLimiter])

  def main(args: Array[String]) {
    val clientIP: String = "127.0.0.1"

    val canIncrement1: Boolean = rateLimiter.increment(clientIP)
    println("1-" + canIncrement1)

    val canIncrement2 = rateLimiter.increment(clientIP)
    println("2-" + canIncrement2)

    val canIncrement3 = rateLimiter.increment(clientIP)
    println("3-" + canIncrement3)

    Thread.sleep(2001)

    val canIncrement4 = rateLimiter.increment(clientIP)
    print("4-" + canIncrement4)
  }
}
