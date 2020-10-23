import axios from 'axios';

const setJWTToken = (token) => {
  console.log('token', token);
  if (token) {
    axios.defaults.headers.common.Authorization = token;
  } else {
    delete axios.defaults.headers.common.Authorization;
  }
};

export default setJWTToken;
