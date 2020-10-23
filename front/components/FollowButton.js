import React, { useCallback } from 'react';
import { Button } from 'antd';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';

import { UNFOLLOW_REQ, FOLLOW_REQ } from '../reducers/user';

const FollowButton = ({ post }) => {
  const dispatch = useDispatch();
  const { me, followLoading, unfollowLoading } = useSelector((state) => state.user);
  const isFollowing = me?.followings.find((v) => v.id === post.account.id);
  const onFollow = useCallback(
    () => {
      if (isFollowing) {
        dispatch({
          type: UNFOLLOW_REQ,
          data: post.account.id,
        });
      } else {
        dispatch({
          type: FOLLOW_REQ,
          data: post.account.id,
        });
      }
    },
    [isFollowing],
  );
  return (
    <Button onClick={onFollow}>
      { isFollowing ? 'UnFollow' : 'Follow'}
    </Button>
  );
};

FollowButton.propTypes = {
  post: PropTypes.object.isRequired,
};

export default FollowButton;
