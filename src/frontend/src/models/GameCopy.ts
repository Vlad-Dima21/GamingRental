export default interface GameCopy {
  gameBase: {
    gameName: string;
    gameGenre: string;
  };
  gameDevice: {
    deviceBaseName: string;
    deviceBaseProducer: string;
    deviceBaseYearOfRelease: number;
  };
  gameId: number;
  gameCopyId: number;
  available: boolean;
}
