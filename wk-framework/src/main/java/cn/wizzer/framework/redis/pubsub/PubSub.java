package cn.wizzer.framework.redis.pubsub;

public interface PubSub {

    void onMessage(String channel, String message);
}
