import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVehicleType } from '../vehicle-type.model';
import { VehicleTypeService } from '../service/vehicle-type.service';
import { VehicleTypeFormGroup, VehicleTypeFormService } from './vehicle-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-type-update',
  templateUrl: './vehicle-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleTypeUpdateComponent implements OnInit {
  isSaving = false;
  vehicleType: IVehicleType | null = null;

  protected vehicleTypeService = inject(VehicleTypeService);
  protected vehicleTypeFormService = inject(VehicleTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleTypeFormGroup = this.vehicleTypeFormService.createVehicleTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleType }) => {
      this.vehicleType = vehicleType;
      if (vehicleType) {
        this.updateForm(vehicleType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleType = this.vehicleTypeFormService.getVehicleType(this.editForm);
    if (vehicleType.id !== null) {
      this.subscribeToSaveResponse(this.vehicleTypeService.update(vehicleType));
    } else {
      this.subscribeToSaveResponse(this.vehicleTypeService.create(vehicleType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleType>>): void {
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

  protected updateForm(vehicleType: IVehicleType): void {
    this.vehicleType = vehicleType;
    this.vehicleTypeFormService.resetForm(this.editForm, vehicleType);
  }
}
