import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicleType, NewVehicleType } from '../vehicle-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleType for edit and NewVehicleTypeFormGroupInput for create.
 */
type VehicleTypeFormGroupInput = IVehicleType | PartialWithRequiredKeyOf<NewVehicleType>;

type VehicleTypeFormDefaults = Pick<NewVehicleType, 'id'>;

type VehicleTypeFormGroupContent = {
  id: FormControl<IVehicleType['id'] | NewVehicleType['id']>;
  name: FormControl<IVehicleType['name']>;
};

export type VehicleTypeFormGroup = FormGroup<VehicleTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleTypeFormService {
  createVehicleTypeFormGroup(vehicleType: VehicleTypeFormGroupInput = { id: null }): VehicleTypeFormGroup {
    const vehicleTypeRawValue = {
      ...this.getFormDefaults(),
      ...vehicleType,
    };
    return new FormGroup<VehicleTypeFormGroupContent>({
      id: new FormControl(
        { value: vehicleTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(vehicleTypeRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getVehicleType(form: VehicleTypeFormGroup): IVehicleType | NewVehicleType {
    return form.getRawValue() as IVehicleType | NewVehicleType;
  }

  resetForm(form: VehicleTypeFormGroup, vehicleType: VehicleTypeFormGroupInput): void {
    const vehicleTypeRawValue = { ...this.getFormDefaults(), ...vehicleType };
    form.reset(
      {
        ...vehicleTypeRawValue,
        id: { value: vehicleTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleTypeFormDefaults {
    return {
      id: null,
    };
  }
}
