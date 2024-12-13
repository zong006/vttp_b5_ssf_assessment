package vttp.batch5.ssf.noticeboard.repositories;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.noticeboard.util.Util;



@Repository
public class NoticeRepository {

	@Autowired
	@Qualifier("notice")
	RedisTemplate<String, Object> objectTemplate;
	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 * ================ corresponding redis-cli command used for task 4 ================
	 * 
	 * hset Util.redisKey id entry
	 * 
	 * * ================ corresponding redis-cli command used for task 4 ================
	 */
	public void insertNotices(String id, String entry) {
		objectTemplate.opsForHash().put(Util.redisKey, id, entry);
	}

	// TODO: Task 6
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 * ================ corresponding redis-cli command used for task 6 ================
	 * 
	 * hrandfield Util.redisKey
	 * 
	 * * ================ corresponding redis-cli command used for task 6 ================
	 */

	public String getRandomKey(){
		return (String) objectTemplate.opsForHash().randomKey(Util.redisKey);
	}

	public boolean keySetLengthIsNull(){
		Set<Object> keyset = objectTemplate.opsForHash().keys(Util.redisKey);
		return keyset.size()==0;
	}


}
