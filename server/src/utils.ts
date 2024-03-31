export const generateUid = (len: number = 8): string => {
  const charset =
    'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  let uid = '';
  for (let i = 0; i < len; i++) {
    uid += charset.charAt(Math.floor(Math.random() * charset.length));
  }
  return uid;
};
