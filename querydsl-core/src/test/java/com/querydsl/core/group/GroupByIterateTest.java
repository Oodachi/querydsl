/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.group;

import static com.querydsl.core.group.GroupBy.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import java.util.*;
import org.junit.Test;

public class GroupByIterateTest extends AbstractGroupByTest {

  @Test
  public void group_order() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(groupBy(postId).iterate(postName, set(commentId)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);
  }

  @Test
  public void first_set_and_list() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(
            groupBy(postId).iterate(postName, set(commentId), list(commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(1);
    assertThat(group.getOne(postId)).isEqualTo(toInt(1));
    assertThat(group.getOne(postName)).isEqualTo("post 1");
    assertThat(group.getSet(commentId)).isEqualTo(toSet(1, 2, 3));
    assertThat(group.getList(commentText))
        .isEqualTo(Arrays.asList("comment 1", "comment 2", "comment 3"));
  }

  @Test
  public void group_by_null() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(
            groupBy(postId).iterate(postName, set(commentId), list(commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(0);
    assertThat(group.getOne(postId)).isNull();
    assertThat(group.getOne(postName)).isEqualTo("null post");
    assertThat(group.getSet(commentId)).isEqualTo(toSet(7, 8));
    assertThat(group.getList(commentText)).isEqualTo(Arrays.asList("comment 7", "comment 8"));
  }

  @Test(expected = NoSuchElementException.class)
  public void noSuchElementException() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(
            groupBy(postId).iterate(postName, set(commentId), list(commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(1);
    group.getSet(qComment);
  }

  @Test(expected = ClassCastException.class)
  public void classCastException() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(
            groupBy(postId).iterate(postName, set(commentId), list(commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(1);
    group.getList(commentId);
  }

  @Test
  public void map1() {
    CloseableIterator<Group> resultsIt =
        MAP_RESULTS.transform(groupBy(postId).iterate(postName, map(commentId, commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(1);
    Map<Integer, String> comments = group.getMap(commentId, commentText);
    assertThat(comments).hasSize(3);
    assertThat(comments).containsEntry(2, "comment 2");
  }

  @Test
  public void map2() {
    CloseableIterator<Map<Integer, String>> resultsIt =
        MAP2_RESULTS.transform(groupBy(postId).iterate(map(commentId, commentText)));
    List<Map<Integer, String>> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Map<Integer, String> comments = results.get(1);
    assertThat(comments).hasSize(3);
    assertThat(comments).containsEntry(2, "comment 2");
  }

  @Test
  public void map22() {
    CloseableIterator<Map<Integer, String>> results =
        MAP2_RESULTS.transform(groupBy(postId).iterate(map(commentId, commentText)));
    List<Map<Integer, String>> actual = IteratorAdapter.asList(results);

    Object commentId = null;
    Map<Integer, String> comments = null;
    List<Map<Integer, String>> expected = new LinkedList<Map<Integer, String>>();
    for (Iterator<Tuple> iterator = MAP2_RESULTS.iterate(); iterator.hasNext(); ) {
      Tuple tuple = iterator.next();
      Object[] array = tuple.toArray();

      if (comments == null
          || !(commentId == array[0] || commentId != null && commentId.equals(array[0]))) {
        comments = new LinkedHashMap<Integer, String>();
        expected.add(comments);
      }
      commentId = array[0];
      @SuppressWarnings("unchecked")
      Pair<Integer, String> pair = (Pair<Integer, String>) array[1];

      comments.put(pair.getFirst(), pair.getSecond());
    }
    assertThat(actual.toString()).isEqualTo(expected.toString());
  }

  @Test
  public void map3() {
    CloseableIterator<Map<Integer, Map<Integer, String>>> results =
        MAP3_RESULTS.transform(groupBy(postId).iterate(map(postId, map(commentId, commentText))));
    List<Map<Integer, Map<Integer, String>>> actual = IteratorAdapter.asList(results);

    Object postId = null;
    Map<Integer, Map<Integer, String>> posts = null;
    List<Map<Integer, Map<Integer, String>>> expected =
        new LinkedList<Map<Integer, Map<Integer, String>>>();
    for (Iterator<Tuple> iterator = MAP3_RESULTS.iterate(); iterator.hasNext(); ) {
      Tuple tuple = iterator.next();
      Object[] array = tuple.toArray();

      if (posts == null || !(postId == array[0] || postId != null && postId.equals(array[0]))) {
        posts = new LinkedHashMap<Integer, Map<Integer, String>>();
        expected.add(posts);
      }
      postId = array[0];
      @SuppressWarnings("unchecked")
      Pair<Integer, Pair<Integer, String>> pair = (Pair<Integer, Pair<Integer, String>>) array[1];
      Integer first = pair.getFirst();
      Map<Integer, String> comments =
          posts.computeIfAbsent(first, k -> new LinkedHashMap<Integer, String>());
      Pair<Integer, String> second = pair.getSecond();
      comments.put(second.getFirst(), second.getSecond());
    }
    assertThat(actual.toString()).isEqualTo(expected.toString());
  }

  @Test
  public void map4() {
    CloseableIterator<Map<Map<Integer, String>, String>> results =
        MAP4_RESULTS.transform(groupBy(postId).iterate(map(map(postId, commentText), postName)));
    List<Map<Map<Integer, String>, String>> actual = IteratorAdapter.asList(results);

    Object commentId = null;
    Map<Map<Integer, String>, String> comments = null;
    List<Map<Map<Integer, String>, String>> expected =
        new LinkedList<Map<Map<Integer, String>, String>>();
    for (Iterator<Tuple> iterator = MAP4_RESULTS.iterate(); iterator.hasNext(); ) {
      Tuple tuple = iterator.next();
      Object[] array = tuple.toArray();

      if (comments == null
          || !(commentId == array[0] || commentId != null && commentId.equals(array[0]))) {
        comments = new LinkedHashMap<Map<Integer, String>, String>();
        expected.add(comments);
      }
      commentId = array[0];
      @SuppressWarnings("unchecked")
      Pair<Pair<Integer, String>, String> pair = (Pair<Pair<Integer, String>, String>) array[1];
      Pair<Integer, String> first = pair.getFirst();
      Map<Integer, String> posts = Collections.singletonMap(first.getFirst(), first.getSecond());
      comments.put(posts, pair.getSecond());
    }
    assertThat(actual.toString()).isEqualTo(expected.toString());
  }

  @Test
  public void array_access() {
    CloseableIterator<Group> resultsIt =
        BASIC_RESULTS.transform(
            groupBy(postId).iterate(postName, set(commentId), list(commentText)));
    List<Group> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Group group = results.get(1);
    Object[] array = group.toArray();
    assertThat(array[0]).isEqualTo(toInt(1));
    assertThat(array[1]).isEqualTo("post 1");
    assertThat(array[2]).isEqualTo(toSet(1, 2, 3));
    assertThat(array[3]).isEqualTo(Arrays.asList("comment 1", "comment 2", "comment 3"));
  }

  @Test
  public void transform_results() {
    CloseableIterator<Post> resultsIt =
        POST_W_COMMENTS.transform(
            groupBy(postId)
                .iterate(Projections.constructor(Post.class, postId, postName, set(qComment))));
    List<Post> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Post post = results.get(1);
    assertThat(post).isNotNull();
    assertThat(post.getId()).isEqualTo(toInt(1));
    assertThat(post.getName()).isEqualTo("post 1");
    assertThat(post.getComments()).isEqualTo(toSet(comment(1), comment(2), comment(3)));
  }

  @Test
  public void transform_as_bean() {
    CloseableIterator<Post> resultsIt =
        POST_W_COMMENTS.transform(
            groupBy(postId)
                .iterate(
                    Projections.bean(Post.class, postId, postName, set(qComment).as("comments"))));
    List<Post> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(4);

    Post post = results.get(1);
    assertThat(post).isNotNull();
    assertThat(post.getId()).isEqualTo(toInt(1));
    assertThat(post.getName()).isEqualTo("post 1");
    assertThat(post.getComments()).isEqualTo(toSet(comment(1), comment(2), comment(3)));
  }

  @Test
  public void oneToOneToMany_projection() {
    CloseableIterator<User> resultsIt =
        USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName)
                .iterate(
                    Projections.constructor(
                        User.class,
                        userName,
                        Projections.constructor(Post.class, postId, postName, set(qComment)))));
    List<User> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(2);

    User user = results.get(0);
    Post post = user.getLatestPost();
    assertThat(post.getId()).isEqualTo(toInt(2));
    assertThat(post.getName()).isEqualTo("post 2");
    assertThat(post.getComments()).isEqualTo(toSet(comment(4), comment(5)));
  }

  @Test
  public void oneToOneToMany_projection_as_bean() {
    CloseableIterator<User> resultsIt =
        USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName)
                .iterate(
                    Projections.bean(
                        User.class,
                        userName,
                        Projections.bean(Post.class, postId, postName, set(qComment).as("comments"))
                            .as("latestPost"))));
    List<User> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(2);

    User user = results.get(0);
    Post post = user.getLatestPost();
    assertThat(post.getId()).isEqualTo(toInt(2));
    assertThat(post.getName()).isEqualTo("post 2");
    assertThat(post.getComments()).isEqualTo(toSet(comment(4), comment(5)));
  }

  @Test
  public void oneToOneToMany_projection_as_bean_and_constructor() {
    CloseableIterator<User> resultsIt =
        USERS_W_LATEST_POST_AND_COMMENTS.transform(
            groupBy(userName)
                .iterate(
                    Projections.bean(
                        User.class,
                        userName,
                        Projections.constructor(Post.class, postId, postName, set(qComment))
                            .as("latestPost"))));
    List<User> results = IteratorAdapter.asList(resultsIt);

    assertThat(results).hasSize(2);

    User user = results.get(0);
    Post post = user.getLatestPost();
    assertThat(post.getId()).isEqualTo(toInt(2));
    assertThat(post.getName()).isEqualTo("post 2");
    assertThat(post.getComments()).isEqualTo(toSet(comment(4), comment(5)));
  }
}
