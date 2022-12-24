import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SocketComponent } from './components/socket/socket.component';

const routes: Routes = [
  { path: '', component: SocketComponent, pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
