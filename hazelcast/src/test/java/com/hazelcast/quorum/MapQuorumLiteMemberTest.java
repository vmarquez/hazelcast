package com.hazelcast.quorum;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.QuorumConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.HazelcastTestSupport;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import com.hazelcast.test.annotation.ParallelTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelTest.class})
public class MapQuorumLiteMemberTest extends HazelcastTestSupport {

    private TestHazelcastInstanceFactory factory;

    @Before
    public void before() {
        factory = createHazelcastInstanceFactory(3);
    }

    @Test(expected = QuorumException.class)
    public void test_readQuorumNotSatisfied_withLiteMembers() {
        Config config = createConfig("r", QuorumType.READ, 3, false);
        Config liteConfig = createConfig("r", QuorumType.READ, 3, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("r").keySet();
    }

    @Test
    public void test_readQuorumSatisfied_withLiteMembers() {
        Config config = createConfig("r", QuorumType.READ, 2, false);
        Config liteConfig = createConfig("r", QuorumType.READ, 2, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("r").keySet();
    }

    @Test(expected = QuorumException.class)
    public void test_readReadWriteQuorumNotSatisfied_withLiteMembers() {
        Config config = createConfig("rw", QuorumType.READ_WRITE, 3, false);
        Config liteConfig = createConfig("rw", QuorumType.READ_WRITE, 3, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("rw").keySet();
    }

    @Test
    public void test_readReadWriteQuorumSatisfied_withLiteMembers() {
        Config config = createConfig("rw", QuorumType.READ_WRITE, 2, false);
        Config liteConfig = createConfig("rw", QuorumType.READ_WRITE, 2, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("rw").keySet();
    }

    @Test(expected = QuorumException.class)
    public void test_readWriteQuorumNotSatisfied_withLiteMembers() {
        Config config = createConfig("w", QuorumType.WRITE, 3, false);
        Config liteConfig = createConfig("w", QuorumType.WRITE, 3, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("w").put(0, 0);
    }

    @Test
    public void test_readWriteQuorumSatisfied_withLiteMembers() {
        Config config = createConfig("w", QuorumType.WRITE, 2, false);
        Config liteConfig = createConfig("w", QuorumType.WRITE, 2, true);
        HazelcastInstance instance = factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(config);
        factory.newHazelcastInstance(liteConfig);

        instance.getMap("w").put(0, 0);
    }

    private Config createConfig(String name, QuorumType type, int size, boolean liteMember) {
        QuorumConfig quorumConfig = new QuorumConfig().setName(name).setType(type).setEnabled(true).setSize(size);
        MapConfig mapConfig = new MapConfig(name);
        mapConfig.setQuorumName(name);
        Config config = new Config();
        config.addQuorumConfig(quorumConfig);
        config.addMapConfig(mapConfig);
        config.setLiteMember(liteMember);
        return config;
    }
}
