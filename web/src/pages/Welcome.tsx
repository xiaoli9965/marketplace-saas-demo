import React from 'react';
import {PageContainer} from '@ant-design/pro-layout';
import {Card, Alert, Typography} from 'antd';
import styles from './Welcome.less';
import {useModel} from "@@/plugin-model/useModel";

export default (): React.ReactNode => {
  const {initialState, setInitialState} = useModel('@@initialState');
  const {currentUser} = initialState
  return (
    <PageContainer>
      <Card>
        <Alert
          message={'青云Saas 接入用例'}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
        <Alert message={`您好: ${currentUser.name}`} type="success"/>
        <br/>
        {currentUser.access === 'admin' ? undefined :
          <Alert message={`您的角色: ${currentUser.access},  所属实例: ${currentUser.instance}`} type="success"/>
        }

      </Card>
    </PageContainer>
  );
};
