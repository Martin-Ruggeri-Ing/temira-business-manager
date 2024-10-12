import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicleType } from '../vehicle-type.model';
import { VehicleTypeService } from '../service/vehicle-type.service';

@Component({
  standalone: true,
  templateUrl: './vehicle-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehicleTypeDeleteDialogComponent {
  vehicleType?: IVehicleType;

  protected vehicleTypeService = inject(VehicleTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
