export const renderStars = (rating: number) => {
  return '★'.repeat(rating) + '☆'.repeat(5 - rating);
};

export const getBookEmoji = (index: number) => {
  return ['📕', '📗', '📘', '📙', '📓'][Number(index) % 5];
};

export const genres = [
  'Fiction',
  'Non-fiction',
  'Fantasy',
  'Dystopian',
  'Romance',
  'Programming',
  'Philosophy',
];
