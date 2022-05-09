import { useIntl } from 'umi';
import { DefaultFooter } from '@ant-design/pro-layout';

export default () => {
  const intl = useIntl();
  return (
    <DefaultFooter
      copyright={`青云 Saas接入用例`}
      links={[
        {
          key: 'QingCloud',
          title: 'QingCloud Technologies',
          href: 'https://www.qingcloud.com',
          blankTarget: true,
        },
        {
          key: 'QingCloudApp',
          title: 'QingCloud AppCenter',
          href: 'https://appcenter.qingcloud.com/',
          blankTarget: true,
        }
      ]}
    />
  );
};
