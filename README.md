# Ratelimiter
This is a simple rolling rate limiter that uses Memcahed and atomic incr. Rolling rate limiter is one of the standard approaches described
in different sources such as [this](http://blog.simonwillison.net/post/57956846132/ratelimitcache) or [this](https://www.binpress.com/tutorial/introduction-to-rate-limiting-with-redis-part-2/166)
 
Usage of a moving window with a configurable count of buckets allows a smoother rate limiting.
Without a moving window of time a rate limiter we could exhaust our limit rather abruptly. 
Let's say in the very first 10 minutes of the hour we max out and start killing the request until the start of the next new hour.  
With a sliding window and multiple buckets, this is smoother distribution of rates since the time window moves continuously. 
