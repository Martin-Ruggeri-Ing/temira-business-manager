import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IVehicleType } from 'app/entities/vehicle-type/vehicle-type.model';
import { VehicleTypeService } from 'app/entities/vehicle-type/service/vehicle-type.service';
import { IVehicleBrand } from 'app/entities/vehicle-brand/vehicle-brand.model';
import { VehicleBrandService } from 'app/entities/vehicle-brand/service/vehicle-brand.service';
import { VehicleService } from '../service/vehicle.service';
import { IVehicle } from '../vehicle.model';
import { VehicleFormGroup, VehicleFormService } from './vehicle-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-update',
  templateUrl: './vehicle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleUpdateComponent implements OnInit {
  isSaving = false;
  vehicle: IVehicle | null = null;

  usersSharedCollection: IUser[] = [];
  vehicleTypesSharedCollection: IVehicleType[] = [];
  vehicleBrandsSharedCollection: IVehicleBrand[] = [];

  protected vehicleService = inject(VehicleService);
  protected vehicleFormService = inject(VehicleFormService);
  protected userService = inject(UserService);
  protected vehicleTypeService = inject(VehicleTypeService);
  protected vehicleBrandService = inject(VehicleBrandService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleFormGroup = this.vehicleFormService.createVehicleFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareVehicleType = (o1: IVehicleType | null, o2: IVehicleType | null): boolean => this.vehicleTypeService.compareVehicleType(o1, o2);

  compareVehicleBrand = (o1: IVehicleBrand | null, o2: IVehicleBrand | null): boolean =>
    this.vehicleBrandService.compareVehicleBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.vehicle = vehicle;
      if (vehicle) {
        this.updateForm(vehicle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.vehicleFormService.getVehicle(this.editForm);
    if (vehicle.id !== null) {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.create(vehicle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehicle: IVehicle): void {
    this.vehicle = vehicle;
    this.vehicleFormService.resetForm(this.editForm, vehicle);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, vehicle.user);
    this.vehicleTypesSharedCollection = this.vehicleTypeService.addVehicleTypeToCollectionIfMissing<IVehicleType>(
      this.vehicleTypesSharedCollection,
      vehicle.type,
    );
    this.vehicleBrandsSharedCollection = this.vehicleBrandService.addVehicleBrandToCollectionIfMissing<IVehicleBrand>(
      this.vehicleBrandsSharedCollection,
      vehicle.brand,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.vehicle?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.vehicleTypeService
      .query()
      .pipe(map((res: HttpResponse<IVehicleType[]>) => res.body ?? []))
      .pipe(
        map((vehicleTypes: IVehicleType[]) =>
          this.vehicleTypeService.addVehicleTypeToCollectionIfMissing<IVehicleType>(vehicleTypes, this.vehicle?.type),
        ),
      )
      .subscribe((vehicleTypes: IVehicleType[]) => (this.vehicleTypesSharedCollection = vehicleTypes));

    this.vehicleBrandService
      .query()
      .pipe(map((res: HttpResponse<IVehicleBrand[]>) => res.body ?? []))
      .pipe(
        map((vehicleBrands: IVehicleBrand[]) =>
          this.vehicleBrandService.addVehicleBrandToCollectionIfMissing<IVehicleBrand>(vehicleBrands, this.vehicle?.brand),
        ),
      )
      .subscribe((vehicleBrands: IVehicleBrand[]) => (this.vehicleBrandsSharedCollection = vehicleBrands));
  }
}
