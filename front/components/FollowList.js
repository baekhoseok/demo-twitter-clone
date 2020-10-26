import React, { useCallback } from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';
import { Button, Card, List } from 'antd';
import { StopOutlined } from '@ant-design/icons';
import { REMOVE_FOLLOWER_REQ, UNFOLLOW_REQ } from '../reducers/user';

const FollowList = ({ header, data }) => {
  const dispatch = useDispatch();
  const onClickStop = (accountId) => () => {
    if (header === 'following') {
      dispatch({
        type: UNFOLLOW_REQ,
        data: accountId,
      });
    }
    if (header === 'follower') {
      dispatch({
        type: REMOVE_FOLLOWER_REQ,
        data: accountId,
      });
    }
  };
  return (
    <List
      style={{ marginBottom: 20 }}
      grid={{ gutter: 4, xs: 2, md: 3 }}
      size="small"
      header={<div>{header}</div>}
      loadMore={(
        <div style={{ textAlign: 'center', margin: '10px 0' }}>
          <Button>More</Button>{' '}
        </div>
      )}
      bordered
      dataSource={data}
      renderItem={(item) => (
        <List.Item style={{ marginTop: 20 }}>
          <Card actions={[<StopOutlined key="stop" type="stop" onClick={onClickStop(item.id)} />]}>
            <Card.Meta description={item.username} />
          </Card>
        </List.Item>
      )}
    />
  );
};

FollowList.propTypes = {
  header: PropTypes.string.isRequired,
  data: PropTypes.array.isRequired,
};

export default FollowList;
