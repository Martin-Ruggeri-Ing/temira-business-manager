import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStatement } from '../statement.model';
import { StatementService } from '../service/statement.service';

@Component({
  standalone: true,
  templateUrl: './statement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StatementDeleteDialogComponent {
  statement?: IStatement;

  protected statementService = inject(StatementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
