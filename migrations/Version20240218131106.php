<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240218131106 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE facture ADD description_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE facture ADD CONSTRAINT FK_FE866410D9F966B FOREIGN KEY (description_id) REFERENCES rendezvous (id)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_FE866410D9F966B ON facture (description_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE facture DROP FOREIGN KEY FK_FE866410D9F966B');
        $this->addSql('DROP INDEX UNIQ_FE866410D9F966B ON facture');
        $this->addSql('ALTER TABLE facture DROP description_id');
    }
}
