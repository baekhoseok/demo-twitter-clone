import React, { useState, useCallback } from 'react';
import { Card, Button, Avatar, List, Comment, Popover } from 'antd';
import PropTypes from 'prop-types';
import {
  RetweetOutlined,
  HeartTwoTone,
  HeartOutlined,
  MessageOutlined,
  EllipsisOutlined,
} from '@ant-design/icons';
import { useSelector, useDispatch } from 'react-redux';
import styled from 'styled-components';
import Link from 'next/link';
import ButtonGroup from 'antd/lib/button/button-group';
import moment from "moment"

import PostImages from './PostImages';
import CommentForm from './CommentForm';
import PostCardContent from './PostCardContent';
import { REMOVE_POST_REQ, UNLIKE_POST_REQ, LIKE_POST_REQ, RETWEET_POST_REQ, UPDATE_POST_REQ } from '../reducers/post';
import FollowButton from './FollowButton';

const CardWrapper = styled.div`
  margin-bottom: 20px;
`;

moment.locale("ko")

const PostCard = ({ post }) => {
  const dispatch = useDispatch();
  const { removePostLoading } = useSelector((state) => state.post);
  const id = useSelector((state) => state.user.me?.id);
  const [commentFormOpened, setCommentFormOpened] = useState(false);
  const [editMode, setEditMode] = useState(false);

  const onClickUpdate = useCallback(() => {
    setEditMode(true);
  }, []);

  const onCancelUpdate = useCallback(() => {
    setEditMode(false);
  }, []);

  const onChangePost = useCallback((editText) => () => {
    dispatch({
      type: UPDATE_POST_REQ,
      data: {
        postId: post.id,
        content: editText,
      },
    });
  }, [post]);
  // const [liked, setLiked] = useState(false);

  const onToggleComment = useCallback(() => {
    setCommentFormOpened((prev) => !prev);
  }, [id]);

  const onLike = useCallback(() => {
    if(!id) {
      return alert("로그인이 필요합니다.")
    }
    return  dispatch({
        type: LIKE_POST_REQ,
        data: post.id,
      })
  }, [id]);
  
  const onUnLike = useCallback(() => {
    if(!id) {
      return alert("로그인이 필요합니다.")
    }
    return  dispatch({
        type: UNLIKE_POST_REQ,
        data: post.id,
      })
  }, [id]);
  
  const onRetweet = useCallback(() => {
    if(!id) {
      return alert("로그인이 필요합니다.")
    }
    return  dispatch({
        type: RETWEET_POST_REQ,
        data: post.id,
      })
  }, [id]);

  const onRemovePost = useCallback(() => {
    if(!id) {
      return alert("로그인이 필요합니다.")
    }
    return dispatch({
      type: REMOVE_POST_REQ,
      data: post.id,
    });
  }, [post.id]);

  const liked = post.likes.find(v => v.accountId === id);
  return (
    <div style={{ marginBottom: 20 }}>
      <Card
        cover={post.images[0] && <PostImages images={post.images} />}
        actions={[
          <RetweetOutlined key="retweet" onClick={onRetweet}/>,
          liked
            ? <HeartTwoTone twoToneColor="#eb2f96" key="heart" onClick={onUnLike} />
            : <HeartOutlined key="heart" onClick={onLike} />,
          <MessageOutlined key="message" onClick={onToggleComment} />,
          <Popover
            key="more"
            content={(
              <Button.Group>
                {id && id === post.account.id ? (
                  <>
                    {!post.retweet && <Button onClick={onClickUpdate}>Edit</Button>}
                    <Button onClick={onRemovePost} loading={removePostLoading}>Delete</Button>
                  </>
                ) : <Button>Notify</Button>}
              </Button.Group>
            )}
          >
            <EllipsisOutlined />
          </Popover>,
        ]}
        title={post.retweet ? `Retweeted by ${post.account.username}.` : null}
        extra={id && id !== post.account.id && <FollowButton post={post} />}
      >
        {post.retweet
        ?
          <Card
          cover={post.retweet.images[0] && <PostImages images={post.retweet.images} />}
          >
              <div style={{float: 'right'}}>{moment(post.createdAt).format("YYYY.MM.DD")}</div>
               <Card.Meta
            avatar={
              <Link href={`/user/${post.retweet.account.id}`} prefetch={false}>
              <a><Avatar>{post.retweet.account.username[0]}</Avatar></a>
            </Link>
            }
            title={post.retweet.account.username}
            description={<PostCardContent postData={post.retweet.content} onChangePost={onChangePost} onCancelUpdate={onCancelUpdate}  />}
          />
            </Card>
        :
            ( 
              <>
              <div style={{float: 'right'}}>{moment(post.createdAt).format("YYYY.MM.DD")}</div>
              <Card.Meta
            avatar={
              <Link href={`/user/${post.account.id}`} prefetch={false}>
              <a><Avatar>{post.account.username[0]}</Avatar></a>
            </Link>
            }
            title={post.account.username}
            description={<PostCardContent editMode={editMode} postData={post.content} onChangePost={onChangePost} onCancelUpdate={onCancelUpdate} />}
          />
              </>
            )
          
        }
      </Card>
      {commentFormOpened && (
        <div>
          <CommentForm post={post} />
          <List
            header={`${post.comments.length} comments`}
            itemLayout="horizontal"
            dataSource={post.comments}
            renderItem={(item) => (
              <li>
                <Comment
                  author={item.account.username}
                  avatar={<Avatar>{item.account.username[0]}</Avatar>}
                  content={item.content}
                />

              </li>
            )}
          />
        </div>
      )}
      {/* <CommentForm />
      <Comments /> */}
    </div>
  );
};

PostCard.propTypes = {
  post: PropTypes.shape({
    id: PropTypes.number,
    User: PropTypes.object,
    content: PropTypes.string,
    createdAt: PropTypes.object,
    comments: PropTypes.arrayOf(PropTypes.object),
    images: PropTypes.arrayOf(PropTypes.object),
    likes: PropTypes.arrayOf(PropTypes.object),
  }).isRequired,
};

export default PostCard;
