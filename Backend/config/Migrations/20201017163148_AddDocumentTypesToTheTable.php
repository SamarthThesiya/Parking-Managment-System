<?php

use Migrations\AbstractMigration;

class AddDocumentTypesToTheTable extends AbstractMigration
{
    /**
     * Change Method.
     *
     * More information on this method is available here:
     * http://docs.phinx.org/en/latest/migrations.html#the-change-method
     *
     * @return void
     */
    public function change()
    {
        $sql = "INSERT INTO public.document_types (\"name\",display_name)
                    VALUES ('land_s_to_n_img','Land image of south to north');
                INSERT INTO public.document_types (\"name\",display_name)
                    VALUES ('land_n_to_s_img','Land image of north to south');
                INSERT INTO public.document_types (\"name\",display_name)
                    VALUES ('land_e_to_w_img','Land image of east to west');
                INSERT INTO public.document_types (\"name\",display_name)
                    VALUES ('land_w_to_e_img','Land image of west to east');
                INSERT INTO public.document_types (\"name\",display_name)
                    VALUES ('vehicle_img','Vehicle Image');";

        $this->execute($sql);
    }
}
