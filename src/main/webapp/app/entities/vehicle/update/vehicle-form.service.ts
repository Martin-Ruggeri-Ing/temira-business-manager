import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicle, NewVehicle } from '../vehicle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicle for edit and NewVehicleFormGroupInput for create.
 */
type VehicleFormGroupInput = IVehicle | PartialWithRequiredKeyOf<NewVehicle>;

type VehicleFormDefaults = Pick<NewVehicle, 'id'>;

type VehicleFormGroupContent = {
  id: FormControl<IVehicle['id'] | NewVehicle['id']>;
  model: FormControl<IVehicle['model']>;
  name: FormControl<IVehicle['name']>;
  user: FormControl<IVehicle['user']>;
  type: FormControl<IVehicle['type']>;
  brand: FormControl<IVehicle['brand']>;
};

export type VehicleFormGroup = FormGroup<VehicleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleFormService {
  createVehicleFormGroup(vehicle: VehicleFormGroupInput = { id: null }): VehicleFormGroup {
    const vehicleRawValue = {
      ...this.getFormDefaults(),
      ...vehicle,
    };
    return new FormGroup<VehicleFormGroupContent>({
      id: new FormControl(
        { value: vehicleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      model: new FormControl(vehicleRawValue.model, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      name: new FormControl(vehicleRawValue.name, {
        validators: [Validators.required],
      }),
      user: new FormControl(vehicleRawValue.user),
      type: new FormControl(vehicleRawValue.type),
      brand: new FormControl(vehicleRawValue.brand),
    });
  }

  getVehicle(form: VehicleFormGroup): IVehicle | NewVehicle {
    return form.getRawValue() as IVehicle | NewVehicle;
  }

  resetForm(form: VehicleFormGroup, vehicle: VehicleFormGroupInput): void {
    const vehicleRawValue = { ...this.getFormDefaults(), ...vehicle };
    form.reset(
      {
        ...vehicleRawValue,
        id: { value: vehicleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleFormDefaults {
    return {
      id: null,
    };
  }
}
