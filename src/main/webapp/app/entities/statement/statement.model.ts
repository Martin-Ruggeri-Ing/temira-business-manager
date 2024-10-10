import dayjs from 'dayjs/esm';
import { ISleepDetector } from 'app/entities/sleep-detector/sleep-detector.model';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { IDriver } from 'app/entities/driver/driver.model';

export interface IStatement {
  id: number;
  dateCreation?: dayjs.Dayjs | null;
  destination?: string | null;
  pathCsv?: string | null;
  sleepDetector?: Pick<ISleepDetector, 'id'> | null;
  vehicle?: Pick<IVehicle, 'id'> | null;
  driver?: Pick<IDriver, 'id'> | null;
}

export type NewStatement = Omit<IStatement, 'id'> & { id: null };
