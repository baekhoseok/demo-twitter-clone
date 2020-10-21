import React, { useMemo } from 'react';
import PropTypes from 'prop-types';
import { Form, Input } from 'antd';

const UsernameEditForm = () => {
  const style = useMemo(() => ({
    marginBottom: '20px',
    border: '1px solid @d9d9d9',
    padding: '30px',
  }));
  return (
    <Form style={style}>
      <Input.Search addonBefore="username" enterButton="edit" />
    </Form>
  );
};

UsernameEditForm.propTypes = {};

export default UsernameEditForm;
