import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import AppLayout from '../components/AppLayout';
import PostForm from '../components/PostForm';
import PostCard from '../components/PostCard';
import { LOAD_POSTS_REQ } from '../reducers/post';

const Home = () => {
  const dispatch = useDispatch();
  const { loginDone } = useSelector((state) => state.user);
  const { posts, hasMorePost, loadPostsLoading } = useSelector((state) => state.post);
  // const [blockLoadPosts, setBlockLoadPosts] = useState(false);
  useEffect(() => {
    dispatch({
      type: LOAD_POSTS_REQ,
    });
  }, []);

  useEffect(() => {
    function onScroll() {
      console.log(window.scrollY, document.documentElement.clientHeight, document.documentElement.scrollHeight);
      if (window.scrollY + document.documentElement.clientHeight > document.documentElement.scrollHeight - 300) {
        if (hasMorePost && !loadPostsLoading) {
          dispatch({
            type: LOAD_POSTS_REQ,
          });
        }
      }
    }
    window.addEventListener('scroll', onScroll);
    return () => {
      window.removeEventListener('scroll', onScroll);
    };
  }, [hasMorePost, loadPostsLoading]);

  return (
    <AppLayout>
      {loginDone && <PostForm />}
      {posts.map((post) => (
        <PostCard key={post.id} post={post} />
      ))}
    </AppLayout>
  );
};

export default Home;
