const SortDirection = {
  Ascending: 'asc',
  Descending: 'desc',

  opposite(value?: string): string {
    switch (value) {
      case 'desc':
        return SortDirection.Ascending;
      default:
        return SortDirection.Descending;
    }
  },
};

export default SortDirection;
