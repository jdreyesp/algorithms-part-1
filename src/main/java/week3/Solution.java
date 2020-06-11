import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        //Input check
        if(s == null) return 0;

        int maxSubstring = 0;
        StringBuilder currentSubstringSB = new StringBuilder();
        Deque<Character> q = new ArrayDeque<>();

        final char[] charArray = s.toCharArray();

        for(int i = 0; i < charArray.length; i++) {
            Character currentChar = new Character(charArray[i]);
            if(currentSubstringSB.indexOf(currentChar.toString()) != -1) {
                //change substring only if gt
                int currentMaxLength = currentSubstringSB.toString().length();
                maxSubstring = Math.max(currentMaxLength, maxSubstring);
                currentSubstringSB = createSubStringSBWithFilter(q, currentChar).append(currentChar);
                q = new ArrayDeque<>();
                for(Character c : currentSubstringSB.toString().toCharArray()) {
                    q.add(c);
                }
            } else {
                currentSubstringSB.append(currentChar);
                q.add(currentChar);
            }
        }

        return Math.max(currentSubstringSB.toString().length(), maxSubstring);
    }

    private StringBuilder createSubStringSBWithFilter(Deque<Character> s, Character c) {
        StringBuilder sb = new StringBuilder();
        for(Character character : s) {
            Character currentChar = s.pollFirst();
            if(currentChar.equals(c)) {
                for(Character remainingChar : s) {
                    sb.append(s.pollFirst());
                }
                return sb;
            }
        }
        return sb;
    }

    public static void main(String[] args) {
//        Solution s = new Solution();
//        System.out.println(s.lengthOfLongestSubstring("bpfbhmipx"));

        List<Character> list = new ArrayList<>();
        list.add('-');
        list.add('1');
        list.add('2');
        list.add('3');

        char[] charArray = new char[] {'a', 'b', 'c'};


    }
}