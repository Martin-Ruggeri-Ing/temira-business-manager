import { IUser } from 'app/entities/user/user.model';
import { VehicleType } from 'app/entities/enumerations/vehicle-type.model';
import { VehicleBrand } from 'app/entities/enumerations/vehicle-brand.model';

export interface IVehicle {
  id: number;
  model?: number | null;
  type?: keyof typeof VehicleType | null;
  brand?: keyof typeof VehicleBrand | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
