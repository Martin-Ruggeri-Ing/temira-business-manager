export interface IVehicleBrand {
  id: number;
  name?: string | null;
}

export type NewVehicleBrand = Omit<IVehicleBrand, 'id'> & { id: null };
