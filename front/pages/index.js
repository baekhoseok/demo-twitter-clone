import React, { useEffect, useState, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import AppLayout from '../components/AppLayout';
import PostForm from '../components/PostForm';
import PostCard from '../components/PostCard';
import { LOAD_POSTS_REQ } from '../reducers/post';

const Home = () => {
  const dispatch = useDispatch();
  const { me } = useSelector((state) => state.user);
  const { posts, hasMorePost, loadPostsLoading, pageIndex } = useSelector((state) => state.post);
  const [canReqPosts, setCanReqPosts] = useState(false);
  // const countRef = useRef([]);
  useEffect(() => {
    dispatch({
      type: LOAD_POSTS_REQ,
      data: { page: pageIndex },
    });
  }, []);

  useEffect(() => {
    setCanReqPosts(!!loadPostsLoading);
    console.log(canReqPosts);
  }, [loadPostsLoading]);

  const onScroll = useCallback(() => {
    // eslint-disable-next-line max-len
    if (window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 300) {
      if (hasMorePost && canReqPosts) {
        setCanReqPosts(true);
        dispatch({
          type: LOAD_POSTS_REQ,
          data: { page: pageIndex + 1 },
        });
      }
    }
  }, [hasMorePost, pageIndex, canReqPosts]);

  useEffect(() => {
    window.addEventListener('scroll', onScroll);
    return () => {
      window.removeEventListener('scroll', onScroll);
      // countRef.current = [];
    };
  }, [hasMorePost, posts.length]);

  return (
    <AppLayout>
      {me && <PostForm />}
      {posts.map((post) => (
        <PostCard key={post.id} post={post} />
      ))}
    </AppLayout>
  );
};

export default Home;
