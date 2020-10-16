import React, { useMemo } from 'react';
import PropTypes from 'prop-types';
import { Form, Input } from 'antd';

const NicknameEditForm = () => {
  const style = useMemo(() => ({
    marginBottom: '20px',
    border: '1px solid @d9d9d9',
    padding: '30px',
  }));
  return (
    <Form style={style}>
      <Input.Search addonBefore="nickname" enterButton="edit" />
    </Form>
  );
};

NicknameEditForm.propTypes = {};

export default NicknameEditForm;
