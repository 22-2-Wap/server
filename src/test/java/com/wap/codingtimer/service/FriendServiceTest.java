package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Friend;
import com.wap.codingtimer.domain.FriendRelation;
import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.repository.FriendRepository;
import com.wap.codingtimer.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FriendServiceTest {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FriendService friendService;

    @Test
    @DisplayName("ViewFriends")
    public void 친구관계_출력() throws Exception {
        //given
        Member[] member = new Member[4];
        Friend[] friend = new Friend[5];
        for(int i =0; i < member.length; i++){
            member[i] = new Member();
            member[i].setNickname(Integer.toString(i));
            memberRepository.save(member[i]);
        }

        friend[0] = new Friend(member[0], member[1], FriendRelation.ACCEPT);
        friend[1]= new Friend(member[0], member[2], FriendRelation.ACCEPT);
        friend[2] = new Friend(member[3], member[0], FriendRelation.ACCEPT);
        friend[3] = new Friend(member[1], member[2], FriendRelation.ACCEPT);
        friend[4] = new Friend(member[3], member[1], FriendRelation.ACCEPT);

        friendRepository.saveAll(Arrays.asList(friend));

        //when
        String[] str = friendService.getFriendsNames(member[0].getId());


        //then
        int i = 1;
        for (String st : str
        ) {
            String stb = Integer.toString(i++);
            assertThat(st).isEqualTo(stb);
        }
    }


   @Test
   @DisplayName("requestList")
   public void 친구요청목록_확인() throws Exception{
       //given
       Member[] member = new Member[4];
       Friend[] friend = new Friend[5];
       for(int i =0; i < member.length; i++){
           member[i] = new Member();
           member[i].setNickname(Integer.toString(i));
           memberRepository.save(member[i]);
       }

       friend[0] = new Friend(member[0], member[1], FriendRelation.ACCEPT);
       friend[1]= new Friend(member[0], member[2], FriendRelation.REQUEST);
       friend[2] = new Friend(member[3], member[0], FriendRelation.ACCEPT);
       friend[3] = new Friend(member[1], member[2], FriendRelation.REQUEST);
       friend[4] = new Friend(member[3], member[1], FriendRelation.REQUEST);

       friendRepository.saveAll(Arrays.asList(friend));

       //when
       String[] str1 = friendService.getRequestNames(member[0].getId());
       String[] str2 = friendService.getRequestNames(member[1].getId());
       String[] str4 = friendService.getRequestNames(member[3].getId());


       //then str1=3, str2=3, str4=2
       assertThat(str1[0]).isEqualTo(Integer.toString(2));
       assertThat(str2[0]).isEqualTo(Integer.toString(2));
       assertThat(str4[0]).isEqualTo(Integer.toString(1));
   }


   @Test
   @DisplayName("friendRequest")
   public void 친구추가_요청() throws Exception{

       //given
       Member[] member = new Member[4];
       Friend[] friend = new Friend[5];
       for(int i =0; i < member.length; i++){
           member[i] = new Member();
           member[i].setNickname(Integer.toString(i));
           memberRepository.save(member[i]);
       }

       friend[0] = new Friend(member[0], member[1], FriendRelation.ACCEPT);
       friend[1]= new Friend(member[0], member[2], FriendRelation.REQUEST);
       friend[2] = new Friend(member[3], member[0], FriendRelation.ACCEPT);
       friend[3] = new Friend(member[1], member[2], FriendRelation.ACCEPT);
       friend[4] = new Friend(member[3], member[1], FriendRelation.REQUEST);

       friendRepository.saveAll(Arrays.asList(friend));

       //when member[1], member[3]
       Friend friend1 = friendService.request(member[1].getId(), "3");

       //then

       //요청할 친구 닉네임이 없을때
//       assertThrows(NoSuchElementException.class, ()->
//               friendService.requestFriend(member[0].getId(), "hello"));

       //요청되거나 이미 친구가 되었을 때
       assertThrows(NoSuchElementException.class, ()->
               friendService.request(member[0].getId(), "2"));

       System.out.println("friend1 = " + friend1.getFirstMember().getNickname());
       System.out.println("friend1 = " + friend1.getSecondMember().getNickname());
       System.out.println("friend1 = " + friend1.getRelation());

   }

   @Test
   @DisplayName("deleteRequest")
   public void 요청_삭제하기() throws Exception{

       //given
       Member[] member = new Member[4];
       Friend[] friend = new Friend[5];
       for(int i =0; i < member.length; i++){
           member[i] = new Member();
           member[i].setNickname(Integer.toString(i));
           memberRepository.save(member[i]);
       }

       friend[0] = new Friend(member[0], member[1], FriendRelation.ACCEPT);
       friend[1]= new Friend(member[0], member[2], FriendRelation.ACCEPT);
       friend[2] = new Friend(member[0], member[3], FriendRelation.ACCEPT);
       friend[3] = new Friend(member[1], member[2], FriendRelation.REQUEST);
       friend[4] = new Friend(member[1], member[3], FriendRelation.REQUEST);

       friendRepository.saveAll(Arrays.asList(friend));

       //when

       //친구가된 친구 삭제하기
       friendService.revert(member[0].getId(), "1");

       //친구 요청에서 요청 거절하기
       friendService.revert(member[1].getId(), "2");
       //then
       for (String str: friendService.getFriendsNames(member[0].getId())
            ) {
           System.out.println("str1 = " + str);
       }
       for (String str: friendService.getRequestNames(member[1].getId())
       ){
           System.out.println("str2 = " + str);
       }
   }


   @Test
   @DisplayName("requestAccept")
   public void 요청수락거절하기() throws Exception{

       //given
       Member[] member = new Member[4];
       Friend[] friend = new Friend[5];
       for(int i =0; i < member.length; i++){
           member[i] = new Member();
           member[i].setNickname(Integer.toString(i));
           memberRepository.save(member[i]);
       }

       friend[0] = new Friend(member[0], member[1], FriendRelation.REQUEST);
       friend[1]= new Friend(member[0], member[2], FriendRelation.ACCEPT);
       friend[2] = new Friend(member[0], member[3], FriendRelation.REQUEST);
       friend[3] = new Friend(member[1], member[2], FriendRelation.ACCEPT);
       friend[4] = new Friend(member[1], member[3], FriendRelation.ACCEPT);

       friendRepository.saveAll(Arrays.asList(friend));

       //when

       //친구 요청을 수락
       friendService.acceptRequest(member[0].getId(), "1");

       //친구 요청을 거절
       friendService.rejectRequest(member[1].getId(), "3");

       //then
       for (String str: friendService.getFriendsNames(member[0].getId())
            ) {
           System.out.println("str1 = " + str);
       }
       for (String str: friendService.getFriendsNames(member[1].getId())
            ) {
           System.out.println("str2 = " + str);
       }
   }
}