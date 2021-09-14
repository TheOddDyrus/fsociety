package com.thomax.letsgo.advanced.arithmetic;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 布隆过滤器主要用于判断一个元素是否在一个集合中，它可以使用一个位数组简洁的表示一个数组。
 * 它的空间效率和查询时间远远超过一般的算法，但是它存在一定的误判的概率，适用于容忍误判的场景。
 * 如果布隆过滤器判断元素存在于一个集合中，那么大概率是存在在集合中，如果它判断元素不存在一个集合中，那么一定不存在于集合中。
 * 所以常常被用于大数据去重。
 */
public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 24; //33554432

    private static final int[] seeds = new int[]{5, 7, 11, 13, 31, 37, 61};

    private final BitSet bitSet = new BitSet(DEFAULT_SIZE);

    private final SimpleHash[] simpleHashes = new SimpleHash[seeds.length];

    /*简易布隆过滤器*/
    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            simpleHashes[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash simpleHash : simpleHashes) {
            bitSet.set(simpleHash.hash(value), true);
        }
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }

        boolean ret = true;
        for (SimpleHash simpleHash : simpleHashes) {
            ret = bitSet.get(simpleHash.hash(value));
            if (!ret) {
                return false;
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("123456");
        list.add("hello");
        list.add("##&");
        list.add("123456");
        list.add("##&");
        list.add("hello");
        list.add("test");

        BloomFilter bloomFilter = new BloomFilter();
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i);
            boolean exist = bloomFilter.contains(value);
            if (exist) {
                System.out.println("list中下标为:" + i + "的值发生重复 " + value);
            } else {
                bloomFilter.add(value);
            }
        }
    }

    private static class SimpleHash {
        private final int cap;
        private final int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }

}

/**
 * 基于REDIS的商用版布隆表达式
 */
class RedisBloomFilter<E> {

    private final RedisTemplate redisTemplate = new RedisTemplate();

    // total length of the Bloom filter
    private final int sizeOfBloomFilter;
    // expected (maximum) number of elements to be added
    private final int expectedNumberOfFilterElements;
    // number of hash functions
    private final int numberOfHashFunctions;
    // encoding used for storing hash values as strings
    private final Charset charset = StandardCharsets.UTF_8;
    // MD5 gives good enough accuracy in most circumstances. Change to SHA1 if it's needed
    private static final String hashName = "MD5";
    private static final MessageDigest digestFunction;

    // The digest method is reused between instances
    static {
        MessageDigest tmp;
        try {
            tmp = java.security.MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        digestFunction = tmp;
    }

    public RedisBloomFilter() {
        this(0.0001, 600000);
    }

    /**
     * Constructs an empty Bloom filter.
     *
     * @param m is the total length of the Bloom filter.
     * @param n is the expected number of elements the filter will contain.
     * @param k is the number of hash functions used.
     */
    public RedisBloomFilter(int m, int n, int k) {
        this.sizeOfBloomFilter = m;
        this.expectedNumberOfFilterElements = n;
        this.numberOfHashFunctions = k;
    }

    /**
     * Constructs an empty Bloom filter with a given false positive probability.
     * The size of bloom filter and the number of hash functions is estimated
     * to match the false positive probability.
     *
     * @param falsePositiveProbability is the desired false positive probability.
     * @param expectedNumberOfElements is the expected number of elements in the Bloom filter.
     */
    public RedisBloomFilter(double falsePositiveProbability, int expectedNumberOfElements) {
        this((int) Math.ceil((int) Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) * expectedNumberOfElements / Math.log(2)), // m = ceil(kn/ln2)
                expectedNumberOfElements,
                (int) Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2)))); // k = ceil(-ln(f)/ln2)
    }

    /**
     * Adds an object to the Bloom filter. The output from the object's
     * toString() method is used as input to the hash functions.
     *
     * @param element is an element to register in the Bloom filter.
     */
    public void add(String key, String element) {
        add(key, element.getBytes(charset));
    }

    /**
     * Adds an array of bytes to the Bloom filter.
     *
     * @param bytes array of bytes to add to the Bloom filter.
     */
    public void add(final String key, byte[] bytes) {
        long startTime1 = System.currentTimeMillis();
        Boolean exists = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes())
        );
        if (Boolean.FALSE.equals(exists)) {
            redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setBit(key.getBytes(), 0, false)
            );
            redisTemplate.expire(key, 2, TimeUnit.DAYS);
        }

        System.out.println("============检查=>>>>>>>>>>>花费时间:" + (System.currentTimeMillis() - startTime1) / 1000.0 + "s");
        final int[] hashes = createHashes(bytes, numberOfHashFunctions);
        long startTime = System.currentTimeMillis();
        redisTemplate.execute(new RedisCallback<Boolean>() {
                                  @Override
                                  public Boolean doInRedis(RedisConnection connection) {
                                      connection.openPipeline();
                                      for (final int hash : hashes) {
                                          connection.setBit(key.getBytes(), Math.abs(hash % sizeOfBloomFilter), true);
                                      }
                                      connection.closePipeline();
                                      return null;
                                  }
                              }
        );
        System.out.println("============hashes=>>>>>>>>>>>花费时间:" + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
    }

    /**
     * Adds all elements from a Collection to the Bloom filter.
     *
     * @param c Collection of elements.
     */
    public void addAll(String key, Collection<String> c) {
        for (String element : c) {
            add(key, element);
        }
    }

    /**
     * Returns true if the element could have been inserted into the Bloom filter.
     * Use getFalsePositiveProbability() to calculate the probability of this
     * being correct.
     *
     * @param element element to check.
     * @return true if the element could have been inserted into the Bloom filter.
     */
    public boolean contains(String key, String element) {
        return contains(key, element.getBytes(charset));
    }

    /**
     * Returns true if the array of bytes could have been inserted into the Bloom filter.
     * Use getFalsePositiveProbability() to calculate the probability of this
     * being correct.
     *
     * @param bytes array of bytes to check.
     * @return true if the array could have been inserted into the Bloom filter.
     */
    public boolean contains(final String key, byte[] bytes) {
        int[] hashes = createHashes(bytes, numberOfHashFunctions);
        for (final int hash : hashes) {
            Boolean exits = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                                                                @Override
                                                                public Boolean doInRedis(RedisConnection connection) {
                                                                    return connection.getBit(key.getBytes(), Math.abs(hash % sizeOfBloomFilter));
                                                                }
                                                            }
            );

            if (!exits) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all the elements of a Collection could have been inserted
     * into the Bloom filter. Use getFalsePositiveProbability() to calculate the
     * probability of this being correct.
     *
     * @param c elements to check.
     * @return true if all the elements in c could have been inserted into the Bloom filter.
     */
    public boolean containsAll(String key, Collection<String> c) {
        for (String element : c) {
            if (!contains(key, element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates digests based on the contents of an array of bytes and splits the result into 4-byte int's and store them in an array. The
     * digest function is called until the required number of int's are produced. For each call to digest a salt
     * is prepended to the data. The salt is increased by 1 for each call.
     *
     * @param data   specifies input data.
     * @param hashes number of hashes/int's to produce.
     * @return array of int-sized hashes
     */
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];

        int k = 0;
        byte salt = 0;
        while (k < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt);
                salt++;
                digest = digestFunction.digest(data);
            }

            for (int i = 0; i < digest.length / 4 && k < hashes; i++) {
                int h = 0;
                for (int j = (i * 4); j < (i * 4) + 4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[k] = h;
                k++;
            }
        }
        return result;
    }

    public int getSizeOfBloomFilter() {
        return this.sizeOfBloomFilter;
    }

    public int getExpectedNumberOfElements() {
        return this.expectedNumberOfFilterElements;
    }

    public int getNumberOfHashFunctions() {
        return this.numberOfHashFunctions;
    }

    /**
     * Compares the contents of two instances to see if they are equal.
     *
     * @param obj is the object to compare to.
     * @return True if the contents of the objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RedisBloomFilter<E> other = (RedisBloomFilter<E>) obj;
        if (this.sizeOfBloomFilter != other.sizeOfBloomFilter) {
            return false;
        }
        if (this.expectedNumberOfFilterElements != other.expectedNumberOfFilterElements) {
            return false;
        }
        if (this.numberOfHashFunctions != other.numberOfHashFunctions) {
            return false;
        }
        return true;
    }

    /**
     * Calculates a hash code for this class.
     *
     * @return hash code representing the contents of an instance of this class.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.sizeOfBloomFilter;
        hash = 61 * hash + this.expectedNumberOfFilterElements;
        hash = 61 * hash + this.numberOfHashFunctions;
        return hash;
    }

    public static void main(String[] args) {
        RedisBloomFilter<String> bloomFilter = new RedisBloomFilter<>(0.0001, 600000);
        System.out.println(bloomFilter.getSizeOfBloomFilter());
        System.out.println(bloomFilter.getNumberOfHashFunctions());
    }

}
