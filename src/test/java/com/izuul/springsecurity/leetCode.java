package com.izuul.springsecurity;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-06 13:57
 **/
public class leetCode {

    class Solution {
        public int[] twoSum(int[] nums, int target) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i];
                if (map.containsKey(complement)) {
                    return new int[] { map.get(complement), i };
                }
                map.put(nums[i], i);
            }
            throw new IllegalArgumentException("No two sum solution");
        }
    }

    @Test
    public void doMain() {
        int[] nums = new int[]{3, 3};
        int target = 6;
        Solution solution = new Solution();
        int[] ints = solution.twoSum(nums, target);
        System.out.println("ints = " + Arrays.toString(ints));
    }
}
