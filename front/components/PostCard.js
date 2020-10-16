import React, { useState, useCallback } from "react";
import { Card, Button, Avatar, List, Comment, Popover } from "antd";
import PropTypes from "prop-types";
import {
  RetweetOutlined,
  HeartTwoTone,
  HeartOutlined,
  MessageOutlined,
  EllipsisOutlined
} from "@ant-design/icons";
import { useSelector, useDispatch } from "react-redux";
import styled from "styled-components";

import ButtonGroup from "antd/lib/button/button-group";
import PostImages from "./PostImages"
import CommentForm from "./CommentForm"
import PostCardContent from "./PostCardContent"
import { REMOVE_POST_REQ } from "../reducers/post";
import FollowButton from "./FollowButton";

const CardWrapper = styled.div`
  margin-bottom: 20px;
`;

const PostCard = ({ post }) => {
  const dispatch = useDispatch();
  const {removePostLoading} = useSelector(state => state.post)
  const id = useSelector(state => state.user.me?.id);
  const [commentFormOpened, setCommentFormOpened] = useState(false);
  const [liked, setLiked] = useState(false);

  const onToggleComment = useCallback(() => {
    setCommentFormOpened((prev) => !prev);
  }, []);

  const onToggleLike = useCallback(() => {
    setLiked((prev) => !prev);
  }, []);
  
  const onRemovePost = useCallback(() => {
    dispatch({
      type: REMOVE_POST_REQ,
      data: post.id
    })
  }, [post.id])
  return (
    <div style={{marginBottom: 20}}>
      <Card
        cover={post.Images[0] && <PostImages images={post.Images}/>}
        actions={[
          <RetweetOutlined key="retweet" />,
          liked 
            ? <HeartTwoTone twoToneColor="#eb2f96" key="heart" onClick={onToggleLike}/>
            : <HeartOutlined key="heart" onClick={onToggleLike}/>,
          <MessageOutlined key="message" onClick={onToggleComment}  />,
          <Popover
            key="more"
            content={
              <Button.Group>
                {id && id === post.User.id ? (
                  <>
                  <Button>Edit</Button>
                  <Button onClick={onRemovePost} loading={removePostLoading}>Delete</Button>
                  </>
                ) : <Button>Notify</Button>}
              </Button.Group>
            }
          >
            <EllipsisOutlined />
          </Popover>
        ]}
        extra={id && <FollowButton post={post}/>}
      >
      <Card.Meta 
        avatar={<Avatar>{post.User.nickname[0]}</Avatar>}
        title={post.User.nickname}
        description={<PostCardContent postData={post.content} />}
      />
      </Card>
      {commentFormOpened && (
        <div>
          <CommentForm post={post}/>
          <List 
            header={`${post.Comments.length} comments`}
            itemLayout="horizontal"
            dataSource={post.Comments}
            renderItem={(item) => (
              <li>
                <Comment
                  author={item.User.nickname}
                  avatar={<Avatar>{item.User.nickname[0]}</Avatar>}
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
    Comments: PropTypes.arrayOf(PropTypes.object),
    Images: PropTypes.arrayOf(PropTypes.object)
  }).isRequired
};

export default PostCard;
