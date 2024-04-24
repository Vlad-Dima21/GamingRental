import Device from './Device';
import GameCopy from './GameCopy';

export default interface Rental {
  rentalDueDate: string;
  rentalReturnDate?: string;
  rentalClient: {
    clientName: string;
    clientEmail: string;
    clientPhone: string;
  };
  rentalDevice: Device;
  rentalGames: GameCopy[];
}
